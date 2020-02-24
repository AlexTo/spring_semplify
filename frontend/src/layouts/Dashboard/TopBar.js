/* eslint-disable no-unused-vars */
import React, {useState, useRef, useEffect} from 'react';
import {Link as RouterLink} from 'react-router-dom';
import {useHistory, useLocation} from 'react-router';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import {useDispatch, useSelector} from 'react-redux';
import {makeStyles} from '@material-ui/styles';
import {
  AppBar,
  Badge,
  Button,
  IconButton,
  Toolbar,
  Hidden,
  Input,
  colors
} from '@material-ui/core';
import NotificationsIcon from '@material-ui/icons/NotificationsOutlined';
import PeopleIcon from '@material-ui/icons/PeopleOutline';
import InputIcon from '@material-ui/icons/Input';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import NotificationsPopover from 'src/components/NotificationsPopover';
import ChatBar from './ChatBar';
import {useKeycloak} from '@react-keycloak/web';
import {searchActions} from "../../actions";

const useStyles = makeStyles((theme) => ({
  root: {
    boxShadow: 'none',
  },
  flexGrow: {
    flexGrow: 1
  },
  search: {
    backgroundColor: 'rgba(255,255,255, 0.1)',
    borderRadius: 4,
    flexBasis: 500,
    height: 36,
    padding: theme.spacing(0, 2),
    display: 'flex',
    alignItems: 'center'
  },
  searchIcon: {
    marginRight: theme.spacing(2),
    color: 'inherit'
  },
  searchInput: {
    flexGrow: 1,
    color: 'inherit',
    '& input::placeholder': {
      opacity: 1,
      color: 'inherit'
    }
  },
  searchPopper: {
    zIndex: theme.zIndex.appBar + 100
  },
  searchPopperContent: {
    marginTop: theme.spacing(1)
  },
  menuButton: {
    marginRight: theme.spacing(1)
  },
  chatButton: {
    marginLeft: theme.spacing(1)
  },
  notificationsButton: {
    marginLeft: theme.spacing(1)
  },
  notificationsBadge: {
    backgroundColor: colors.orange[600]
  },
  logoutButton: {
    marginLeft: theme.spacing(1)
  },
  logoutIcon: {
    marginRight: theme.spacing(1)
  }
}));

function TopBar({onOpenNavBarMobile, className, ...rest}) {
  const {keycloak} = useKeycloak();
  const classes = useStyles();
  const history = useHistory();
  const location = useLocation();
  const searchRef = useRef(null);
  const dispatch = useDispatch();
  const searchReducer = useSelector(state => state.searchReducer);
  const notificationsRef = useRef(null);

  const [notifications, setNotifications] = useState([]);
  const [openNotifications, setOpenNotifications] = useState(false);
  const [openChatBar, setOpenChatBar] = useState(false);

  const handleLogout = () => {
    keycloak.logout().then(() => {
    });
  };

  const handleChatBarOpen = () => {
    setOpenChatBar(true);
  };

  const handleChatBarClose = () => {
    setOpenChatBar(false);
  };

  const handleNotificationsOpen = () => {
    setOpenNotifications(true);
  };

  const handleNotificationsClose = () => {
    setOpenNotifications(false);
  };

  const handleSearch = (event) => {
    if (!event.target.value) {
      return;
    }
    if (event.key === 'Enter') {
      dispatch(searchActions.search(event.target.value));
      if (location.pathname !== "/search") {
        history.push("/search");
      }
    }
  };

  return (
    <AppBar
      {...rest}
      className={clsx(classes.root, className)}
      color="primary"
    >
      <Toolbar>
        <Hidden lgUp>
          <IconButton
            className={classes.menuButton}
            color="inherit"
            onClick={onOpenNavBarMobile}
          >
            <MenuIcon/>
          </IconButton>
        </Hidden>
        <RouterLink to="/">
          <img
            alt="Logo"
            src="/images/logos/logo--white.png"
          />
        </RouterLink>
        <div className={classes.flexGrow}/>
        <Hidden smDown>
          <div
            className={classes.search}
            ref={searchRef}>
            <SearchIcon className={classes.searchIcon}/>
            <Input
              className={classes.searchInput}
              disableUnderline
              onKeyDown={handleSearch}
              placeholder="Search"
              defaultValue={searchReducer.query}/>
          </div>
        </Hidden>
        <IconButton
          className={classes.chatButton}
          color="inherit"
          onClick={handleChatBarOpen}>
          <Badge
            badgeContent={6}
            color="secondary">
            <PeopleIcon/>
          </Badge>
        </IconButton>
        <Hidden mdDown>
          <IconButton
            className={classes.notificationsButton}
            color="inherit"
            onClick={handleNotificationsOpen}
            ref={notificationsRef}>
            <Badge
              badgeContent={notifications.length}
              classes={{badge: classes.notificationsBadge}}
              variant="dot">
              <NotificationsIcon/>
            </Badge>
          </IconButton>
          <Button
            className={classes.logoutButton}
            color="inherit"
            onClick={handleLogout}>
            <InputIcon className={classes.logoutIcon}/>
            Sign out
          </Button>
        </Hidden>
      </Toolbar>
      <NotificationsPopover
        anchorEl={notificationsRef.current}
        notifications={notifications}
        onClose={handleNotificationsClose}
        open={openNotifications}/>
    </AppBar>
  );
}

TopBar.propTypes = {
  className: PropTypes.string,
  onOpenNavBarMobile: PropTypes.func
};

export default TopBar;
