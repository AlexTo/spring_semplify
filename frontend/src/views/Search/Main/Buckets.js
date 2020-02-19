import React, {useEffect} from 'react';
import PropTypes from 'prop-types';
import {makeStyles} from '@material-ui/core/styles';
import TreeView from '@material-ui/lab/TreeView';
import TreeItem from '@material-ui/lab/TreeItem';
import Typography from '@material-ui/core/Typography';
import MailIcon from '@material-ui/icons/Mail';
import DeleteIcon from '@material-ui/icons/Delete';
import Label from '@material-ui/icons/Label';
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import InfoIcon from '@material-ui/icons/Info';
import ForumIcon from '@material-ui/icons/Forum';
import LocalOfferIcon from '@material-ui/icons/LocalOffer';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';
import clsx from "clsx";
import {Checkbox} from "@material-ui/core";

const useTreeItemStyles = makeStyles(theme => ({
  root: {
    color: theme.palette.text.secondary,
    '&:focus > $content': {
      backgroundColor: `var(--tree-view-bg-color, ${theme.palette.grey[400]})`,
      color: 'var(--tree-view-color)',
    },
  },
  content: {
    color: theme.palette.text.secondary,
    borderTopRightRadius: theme.spacing(2),
    borderBottomRightRadius: theme.spacing(2),
    paddingRight: theme.spacing(1),
    fontWeight: theme.typography.fontWeightMedium,
    '$expanded > &': {
      fontWeight: theme.typography.fontWeightRegular,
    },
  },
  group: {
    marginLeft: 0,
    '& $content': {
      paddingLeft: theme.spacing(2),
    },
  },
  expanded: {},
  label: {
    fontWeight: 'inherit',
    color: 'inherit',
  },
  labelRoot: {
    display: 'flex',
    alignItems: 'center',
  },
  labelIcon: {
    marginRight: theme.spacing(1),
  },
  labelText: {
    fontWeight: 'inherit',
    flexGrow: 1,
    textTransform: 'capitalize'
  },
}));

function StyledTreeItem(props) {
  const classes = useTreeItemStyles();
  const {onCheckboxChange, checked, parentNodeId, nodeId, labelText, labelIcon: LabelIcon, labelInfo, color, bgColor, ...other} = props;

  return (
    <TreeItem
      nodeId={nodeId}
      label={
        <div className={classes.labelRoot}>
          <Checkbox
            defaultChecked={checked}
            checked={checked}
            onChange={() => onCheckboxChange(nodeId, parentNodeId)}
            onClick={e => (e.stopPropagation())}/>
          <Typography variant="body2" className={classes.labelText}>
            {labelText}
          </Typography>
          <Typography variant="caption" color="inherit">
            {labelInfo}
          </Typography>
        </div>
      }
      style={{
        '--tree-view-color': color,
        '--tree-view-bg-color': bgColor,
      }}
      classes={{
        root: classes.root,
        content: classes.content,
        expanded: classes.expanded,
        group: classes.group,
        label: classes.label,
      }}
      {...other}
    />
  );
}

StyledTreeItem.propTypes = {
  bgColor: PropTypes.string,
  color: PropTypes.string,
  labelIcon: PropTypes.elementType.isRequired,
  labelInfo: PropTypes.any,
  labelText: PropTypes.string.isRequired,
};

const useStyles = makeStyles((theme) => ({
  root: {
    marginTop: theme.spacing(1.5)
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    marginBottom: theme.spacing(2)
  },
  title: {
    position: 'relative',
    '&:after': {
      position: 'absolute',
      bottom: -8,
      left: 0,
      content: '" "',
      height: 3,
      width: 48,
      backgroundColor: theme.palette.primary.main
    }
  }
}));

export default function Buckets({buckets}) {
  const classes = useStyles();

  const handleBucketCheckboxChange = (uri) => {

  };

  return (
    <div className={clsx(classes.root)}>
      <div className={classes.header}>
        <Typography
          className={classes.title}
          variant="h5">
          Refine
        </Typography>
      </div>
      <TreeView
        className={classes.root}
        defaultExpanded={['3']}
        defaultCollapseIcon={<ArrowDropDownIcon/>}
        defaultExpandIcon={<ArrowRightIcon/>}
        defaultEndIcon={<div style={{width: 24}}/>}>
        {buckets.map(bucket =>
          <StyledTreeItem
            key={bucket.uri}
            nodeId={bucket.uri}
            labelText={bucket.name}
            labelInfo={bucket.docCount}
            labelIcon={Label}
            checked={bucket.checked}
            onCheckboxChange={handleBucketCheckboxChange}>
            {bucket.buckets.map(subBucket =>
              <StyledTreeItem
                key={subBucket.uri}
                nodeId={subBucket.uri}
                parentNodeId={bucket.uri}
                labelText={subBucket.name}
                labelInfo={subBucket.docCount}
                labelIcon={Label}
                checked={subBucket.checked}
                onCheckboxChange={handleBucketCheckboxChange}
              />)}
          </StyledTreeItem>
        )}
      </TreeView>
    </div>
  );
}
